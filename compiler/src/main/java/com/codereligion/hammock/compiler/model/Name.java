package com.codereligion.hammock.compiler.model;

import com.google.common.base.Objects;

public class Name {

    private final String qualifiedName;

    public Name(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }

    private int indexOfLastDot() {
        return qualifiedName.lastIndexOf('.');
    }

    public String getPackage() {
        final int dot = indexOfLastDot();
        return dot == -1 ? "" : qualifiedName.substring(0, dot);
    }

    public String getSimple() {
        final int dot = indexOfLastDot();
        return dot == -1 ? qualifiedName : qualifiedName.substring(dot + 1);
    }

    public String getQualified() {
        return qualifiedName;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        } else if (that instanceof Name) {
            final Name other = (Name) that;
            return qualifiedName.equals(other.getQualified());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(qualifiedName);
    }
    
}