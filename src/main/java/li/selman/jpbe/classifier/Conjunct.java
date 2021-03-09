/*
 * (c) Copyright 2021 Hasan Selman Kara. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package li.selman.jpbe.classifier;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * List of conjunct predicates.
 *
 * @author Hasan Selman Kara
 */
class Conjunct {

    private final List<Predicate> predicates;

    Conjunct(List<Predicate> predicates) {
        if (predicates.isEmpty()) throw new IllegalArgumentException("Predicates can't be empty");

        this.predicates = predicates;
    }

    /**
     * Checks whether the conjuct matches.
     * @return {@code true} if all predicates match {@code s}, else {@code false}
     */
    boolean matches(String s) {
        return predicates.stream()
            .map(predicate -> predicate.matches(s))
            .noneMatch(aBoolean -> Objects.equals(aBoolean, Boolean.FALSE));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Conjunct conjunct = (Conjunct) o;

        return predicates.equals(conjunct.predicates);
    }

    @Override
    public int hashCode() {
        return predicates.hashCode();
    }

    @Override
    public String toString() {
        String dnfString = predicates.stream().map(Object::toString).collect(Collectors.joining(" v "));
        return "(" + dnfString + ")";
    }
}
