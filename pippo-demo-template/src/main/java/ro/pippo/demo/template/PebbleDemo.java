/*
 * Copyright (C) 2014 the original author or authors.
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
package ro.pippo.demo.template;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.extension.AbstractExtension;
import com.mitchellbosecke.pebble.extension.Filter;
import ro.pippo.core.Application;
import ro.pippo.core.Pippo;
import ro.pippo.pebble.PebbleTemplateEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author James Moger
 */
public class PebbleDemo {

    public static void main(String[] args) {
        // .peb is the default file extension
//        Pippo pippo = new Pippo(new TemplateApplication(new PebbleTemplateEngine(), "pebble/hello"));
        Pippo pippo = new Pippo(new TemplateApplication(new MyPebbleTemplateEngine(), "pebble/hello"));
        pippo.start();
    }

    public static class MyPebbleTemplateEngine extends PebbleTemplateEngine {

        @Override
        protected void init(Application application, PebbleEngine.Builder builder) {
            builder.extension(new AbstractExtension() {

                @Override
                public Map<String, Filter> getFilters() {
                    Map<String, Filter> filters = new HashMap<>();
                    filters.put("myupper", new MyUpperFilter());

                    return filters;
                }

            });
        }

    }

    public static class MyUpperFilter implements Filter {

        @Override
        public List<String> getArgumentNames() {
            return null;
        }

        @Override
        public Object apply(Object input, Map<String, Object> args){
            return (input != null) ? ((String) input).toUpperCase() : null;
        }

    }

}
