/*
 * (c) Copyright 2021 Palantir Technologies Inc. All rights reserved.
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

package li.selman.jpbe;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class Foo {

    @Test
    void bar() {
        String[] names1 = new String[] {"Ava", "Emma", "Olivia"};
        String[] names2 = new String[] {"Olivia", "Sophia", "Emma"};
        String[] expected = new String[] {"Ava", "Olivia", "Sophia", "Emma"};

        String[] actual = uniqueNames(names1, names2);
        assertThat(actual).hasSameElementsAs(Arrays.asList(expected.clone()));
    }

    public static String[] uniqueNames(String[] names1, String[] names2) {
        Set<String> s1 = new HashSet<>(Arrays.asList(names1));
        s1.addAll(Arrays.asList(names2));
        return s1.toArray(String[]::new);
    }
}
