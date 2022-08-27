package com.scaffold.algorithm.interview;

import java.util.LinkedList;

/**
 * 二叉树操作
 *
 * @author hui.zhang
 * @date 2022年07月27日 18:14
 */
public class BinaryTree {


    /**
     * 二叉树两个节点的最近父节点
     *
     * @param head
     * @param o1
     * @param o2
     * @return
     */
    private Node lowestParent(Node head, Node o1, Node o2) {
        if (head == null || head == o1 || head == o2) {
            return head;
        }
        Node left = lowestParent(head.left, o1, o2);
        Node right = lowestParent(head.right, o1, o2);
        if (left != null && right != null) {
            return head;
        }
        return left != null ? left : right;
    }


    /**
     * 二叉树层序遍历
     *
     * @param root
     */
    public static void levelIterater(Node root) {
        if (root == null) {
            return;
        }
        LinkedList<Node> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            Node cur = queue.poll();
            System.out.println("-->" + cur.val);
            if (cur.left != null) {
                queue.offer(cur.left);
            }
            if (cur.right != null) {
                queue.offer(cur.right);
            }
        }
    }


    public static class Node {
        private int val;
        private Node left;
        private Node right;

        public Node(int val) {
            this.val = val;
        }
    }
}
