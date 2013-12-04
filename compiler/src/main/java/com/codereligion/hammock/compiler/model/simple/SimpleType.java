package com.codereligion.hammock.compiler.model.simple;

import com.codereligion.hammock.compiler.model.api.Closure;
import com.codereligion.hammock.compiler.model.api.Name;
import com.codereligion.hammock.compiler.model.api.Type;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableSet;

import javax.annotation.Generated;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.in;
import static com.google.common.base.Predicates.not;
import static com.google.common.collect.Iterables.all;
import static com.google.common.collect.Iterables.any;
import static com.google.common.collect.Iterables.removeIf;

public class SimpleType implements Type {

    private final Name name;
    private final List<Closure> closures = new ArrayList<>();

    public SimpleType(Name name) {
        this.name = name;
    }

    @Override
    public String getPackage() {
        return name.getPackage();
    }

    @Override
    public Name getName() {
        return name;
    }

    @Override
    public Iterable<String> getImports() {
        final Set<String> imports = new TreeSet<>();

        for (Closure closure : closures) {
            imports.add(closure.getParameterType().getQualified());
            imports.add(closure.getReturnType().getQualified());
        }

        if (any(closures, Is.PREDICATE)) {
            imports.add(Predicate.class.getName());
        }

        if (any(closures, Is.FUNCTION)) {
            imports.add(Function.class.getName());
        }
        
        if (any(closures, Is.NULLSAFE)) {
            imports.add(Nullable.class.getName());
        }

        if (any(closures, not(Is.NULLSAFE))) {
            imports.add(Preconditions.class.getName());
        }
        
        imports.add(Generated.class.getName());

        removeIf(imports, Exclude.JAVA_LANG);
        removeIf(imports, Exclude.BOOLEAN);

        final String samePackage = name.getPackage();
        removeIf(imports, startsWith(samePackage));
        
        return imports;
    }

    @Override
    public List<Closure> getClosures() {
        return closures;
    }

    private enum Is implements Predicate<Closure> {

        PREDICATE {
            
            @Override
            public boolean apply(@Nullable Closure input) {
                return input != null && input.isPredicate();
            }

        },

        FUNCTION {
            
            @Override
            public boolean apply(@Nullable Closure input) {
                return input != null && !input.isPredicate();
            }

        },
        
        NULLSAFE {
            
            @Override
            public boolean apply(@Nullable Closure input) {
                return input != null && input.isNullsafe();
            }
            
        }

    }

    private enum Exclude implements Predicate<String> {

        JAVA_LANG {
            
            @Override
            public boolean apply(@Nullable String input) {
                return input != null && input.startsWith("java.lang.");
            }
            
        },
        
        BOOLEAN {

            @Override
            public boolean apply(@Nullable String input) {
                return "boolean".equals(input);
            }
            
        }

    }
    
    private static Predicate<String> startsWith(final String prefix) {
        return new Predicate<String>() {
            @Override
            public boolean apply(@Nullable String input) {
                return input != null && input.startsWith(prefix);
            }
        };
    }

}
