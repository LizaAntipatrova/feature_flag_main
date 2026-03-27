package org.redflag.service.util;

import org.redflag.model.OrganizationNode;

import java.util.List;

public class LtreePathUtil {

    public static final char PATH_SEPARATOR = '.';

    public static void replaceSubtreeNodesParentPath(
            List<OrganizationNode> subtreeNodes,
            String oldRootNodePath,
            String parentPath
    ) {
        String oldParentPath = getParentPath(oldRootNodePath);
        subtreeNodes.forEach(node -> node.setPath(node.getPath().replace(oldParentPath, parentPath)));
    }

    public static String getParentPath(String nodePath) {
        int index = nodePath.lastIndexOf(PATH_SEPARATOR);
        if (index == -1) {
            return null;
        }
        return nodePath.substring(0, index);
    }

    public static String getChildPath(String parentNodePath, Long childNodeId) {
        return parentNodePath + PATH_SEPARATOR + childNodeId.toString();
    }
}
