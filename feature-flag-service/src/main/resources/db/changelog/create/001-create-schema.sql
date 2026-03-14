create extension if not exists ltree;

create or replace function set_updated_at()
returns trigger
language plpgsql
as $$
begin
  new.updated_at := now();
return new;
end;
$$;

create table organization(
    id bigserial primary key,
    name varchar(1024) unique not null,
    created_at timestamp default now(),
    updated_at timestamp
);

create trigger organization_set_updated_at
    before update on organization
    for each row execute function set_updated_at();

comment on table organization is 'Организация';
comment on column organization.id is 'id организации';
comment on column organization.name is 'Название организации';
comment on column organization.created_at is 'Дата создания организации';
comment on column organization.created_at is 'Дата последнего обновления организации';

create table organization_node(
    id bigserial primary key,
    name varchar(1024) not null,
    uuid uuid not null,
    organization_id bigint not null references organization(id),
    path ltree,
    is_service boolean not null,
    version bigint not null,
    created_at timestamp default now(),
    updated_at timestamp
);

create trigger organization_node_set_update_at
    before update on organization_node
    for each row execute function set_updated_at();

comment on table organization_node is 'Звено в дереве иерархии организации';
comment on column organization_node.id is 'Id звена организации';
comment on column organization_node.name is 'Название звена организации';
comment on column organization_node.uuid is 'Уникальный не технический идентификатор звена организации';
comment on column organization_node.organization_id is 'Id организации, к которой принадлежит звено';
comment on column organization_node.path is 'Путь до текущего звена в древовидной иерархии организации';
comment on column organization_node.is_service is 'Является ли текущее звено сервисом';
comment on column organization_node.version is 'Версия данных для оптимистичной блокировки';
comment on column organization_node.created_at is 'Дата создания звена организации';
comment on column organization_node.updated_at is 'Дата последнего изменения звена организации';



create table feature_flag(
    id bigserial primary key,
    name varchar(1024) not null,
    value boolean not null,
    organization_node_id bigint not null references organization_node(id),
    version bigint not null ,
    created_at timestamp default now(),
    updated_at timestamp
);

create trigger feature_flag_set_updated_at
    before update on feature_flag
    for each row execute  function set_updated_at();

comment on table feature_flag is 'Фича флаг';
comment on column feature_flag.id is 'Id фича флага';
comment on column feature_flag.name is 'Название фича флага';
comment on column feature_flag.value is 'Значение фича флага';
comment on column feature_flag.organization_node_id is 'Id звена организации, к которому принадлежит фича флаг';
comment on column feature_flag.version is 'Версия данных для оптимистичной блокировки';
comment on column feature_flag.created_at is 'Дата создания фича флага';
comment on column feature_flag.updated_at is 'Дата последнего изменения фича флага';

create index organization_node_gist on organization_node using gist(path);