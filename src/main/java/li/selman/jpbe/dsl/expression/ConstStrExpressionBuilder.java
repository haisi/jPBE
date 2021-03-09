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
package li.selman.jpbe.dsl.expression;

import java.util.List;
import li.selman.jpbe.dsl.Expression;
import li.selman.jpbe.dsl.ExpressionBuilder;

/**
 * @author Hasan Selman Kara
 */
public class ConstStrExpressionBuilder implements ExpressionBuilder {

    @Override
    public List<Expression> computeExpressions(String input, String substr) {
        return List.of(new ConstantStringExpression(substr));
    }

}
