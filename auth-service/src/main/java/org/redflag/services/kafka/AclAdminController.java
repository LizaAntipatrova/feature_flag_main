//package org.redflag.services.kafka;
//
//import io.micronaut.http.HttpResponse;
//import io.micronaut.http.annotation.Controller;
//import io.micronaut.http.annotation.Delete;
//import io.micronaut.http.annotation.Post;
//import io.micronaut.http.annotation.QueryValue;
//import io.micronaut.security.annotation.Secured;
//import io.micronaut.security.rules.SecurityRule;
//import lombok.RequiredArgsConstructor;
//import org.redflag.services.kafkaServices.AclKafkaService;
//
//@Controller("/acls")
//@RequiredArgsConstructor
//@Secured(SecurityRule.IS_ANONYMOUS)
//public class AclAdminController {
//
//    private final AclKafkaService aclKafkaService;
//
//    @Post("/sdk/consumer")
//    public HttpResponse<Void> createSdkConsumerAcls(
//            @QueryValue String username,
//            @QueryValue String topicName,
//            @QueryValue String groupName
//    ) throws Exception {
//        aclKafkaService.createSdkConsumerAcls(username, topicName, groupName);
//        return HttpResponse.ok();
//    }
//
//    @Delete("/sdk/consumer")
//    public HttpResponse<Void> deleteSdkConsumerAcls(
//            @QueryValue String username,
//            @QueryValue String topicName,
//            @QueryValue String groupName
//    ) throws Exception {
//        aclKafkaService.deleteSdkConsumerAcls(username, topicName, groupName);
//        return HttpResponse.ok();
//    }
//
//    @Post("/sdk/consumer/group-prefix")
//    public HttpResponse<Void> createSdkConsumerAclsWithGroupPrefix(
//            @QueryValue String username,
//            @QueryValue String topicName,
//            @QueryValue String groupPrefix
//    ) throws Exception {
//        aclKafkaService.createSdkConsumerAclsWithGroupPrefix(username, topicName, groupPrefix);
//        return HttpResponse.ok();
//    }
//
//    @Delete("/sdk/consumer/group-prefix")
//    public HttpResponse<Void> deleteSdkConsumerAclsWithGroupPrefix(
//            @QueryValue String username,
//            @QueryValue String topicName,
//            @QueryValue String groupPrefix
//    ) throws Exception {
//        aclKafkaService.deleteSdkConsumerAclsWithGroupPrefix(username, topicName, groupPrefix);
//        return HttpResponse.ok();
//    }
//
//    @Post("/main/producer/topic")
//    public HttpResponse<Void> createMainProducerAclsForTopic(
//            @QueryValue String topicName
//    ) throws Exception {
//        aclKafkaService.createMainProducerAclsForTopic(topicName);
//        return HttpResponse.ok();
//    }
//
//    @Delete("/main/producer/topic")
//    public HttpResponse<Void> deleteMainProducerAclsForTopic(
//            @QueryValue String topicName
//    ) throws Exception {
//        aclKafkaService.deleteMainProducerAclsForTopic(topicName);
//        return HttpResponse.ok();
//    }
//
//    @Post("/main/producer/prefix")
//    public HttpResponse<Void> createMainProducerPrefixAcls(
//            @QueryValue String topicPrefix
//    ) throws Exception {
//        aclKafkaService.createMainProducerPrefixAcls(topicPrefix);
//        return HttpResponse.ok();
//    }
//
//    @Post("/main/producer/idempotent-write")
//    public HttpResponse<Void> createMainIdempotentWriteAcl() throws Exception {
//        aclKafkaService.createMainIdempotentWriteAcl();
//        return HttpResponse.ok();
//    }
//}
