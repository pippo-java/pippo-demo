/*
 * Copyright (C) 2017 the original author or authors.
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
package ro.pippo.demo.controller;

import ro.pippo.controller.Controller;
import ro.pippo.controller.GET;
import ro.pippo.controller.POST;
import ro.pippo.controller.Path;
import ro.pippo.controller.Produces;
import ro.pippo.core.FileItem;

import java.io.File;

/**
 * @author Decebal Suiu
 */
@Path("/files")
public class FilesController extends Controller {

    @GET
    public String index() {
        return getRouteContext().renderToString("files");
    }

    @GET("/download")
    public File download() {
        return new File("pom.xml");
    }

    @POST("/upload")
    @Produces(Produces.TEXT)
    public String upload(FileItem file) {
//        return file.toString();
        return new StringBuilder()
            .append(file.getName()).append("\n")
            .append(file.getSubmittedFileName()).append("\n")
            .append(file.getSize()).append("\n")
            .append(file.getContentType())
            .toString();
    }

}
