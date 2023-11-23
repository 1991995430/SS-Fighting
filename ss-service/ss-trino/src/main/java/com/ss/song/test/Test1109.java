package com.ss.song.test;

import com.ss.song.pojo.Node;

import java.util.*;

/**
 * author shangsong 2023/11/9
 */
public class Test1109 {

    public static void main(String[] args) {

        Node ast = new Node();
        ast.setName("parentName");
        ast.setContent("QUERY");

        List<Node> nodeList1 = new ArrayList<>();
        Node nodeParent1 = new Node();
        nodeParent1.setName("child1");
        nodeParent1.setContent("INSERT");

        Node nodeParent2 = new Node();
        nodeParent2.setName("child1");
        nodeParent2.setContent("FROM");

        nodeList1.add(nodeParent1);
        nodeList1.add(nodeParent2);
        ast.setChild(nodeList1);

        List<Node> nodeList2 = new ArrayList<>();
        Node nodeChild1 = new Node();
        nodeChild1.setName("child1");
        nodeChild1.setContent("DESITINATION");

        Node nodeChild2 = new Node();
        nodeChild2.setName("child1");
        nodeChild2.setContent("SELECT");

        nodeList2.add(nodeChild1);
        nodeList2.add(nodeChild2);

        nodeParent1.setChild(nodeList2);


        Deque<Node> stack = new ArrayDeque<Node>();

        stack.push(ast);

        while (!stack.isEmpty()) {
            Node next = stack.pop();
            int child_count = next.getChildCount();
            for (int child_pos = 0; child_pos < child_count; ++child_pos) {
                Node node = next.getChild(child_pos);
                System.out.println(node.getContent());
            }

            List<Node> childrenList = next.getChild();
            if (childrenList != null && childrenList.size() > 0) {
                for (int i = childrenList.size() - 1; i >= 0; i--) {
                    stack.push((Node)childrenList.get(i));
                }
            }
        }

    }

}
