package com.ss.song.pojo;

import java.util.List;

/**
 * author shangsong 2023/11/9
 */
public class Node {
    private String name;

    private String content;

    private List<Node> child;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Node> getChild() {
        return child;
    }

    public Node getChild(int i) {
        return this.child != null && i < this.child.size() ? this.child.get(i) : null;
    }

    public void setChild(List<Node> child) {
        this.child = child;
    }

    public int getChildCount() {
        return this.child == null ? 0 : this.child.size();
    }
}
