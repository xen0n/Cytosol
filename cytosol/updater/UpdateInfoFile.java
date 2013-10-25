/*
 * Copyright 2013 JNRain
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package name.xen0n.cytosol.updater;

import java.io.File;
import java.io.IOException;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.MappingJsonFactory;

import android.content.Context;


public class UpdateInfoFile {
    public static UpdateInfo fromFile(Context ctx) {
        return fromFile(new File(
                ctx.getCacheDir(),
                UpdateManagerManager.getUpdateManager().UPDATE_INFO_CACHE_FILENAME));
    }

    public static UpdateInfo fromFile(File file) {
        MappingJsonFactory factory = new MappingJsonFactory();
        JsonParser parser;
        try {
            parser = factory.createJsonParser(file);
        } catch (JsonParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

        try {
            return parser.readValueAs(UpdateInfo.class);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } finally {
            try {
                // File is owned by parser, so it'll be properly
                // closed by parser whether exception happened or not
                parser.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static void toFile(Context ctx, UpdateInfo updInfo) {
        toFile(
                ctx,
                new File(ctx.getCacheDir(), UpdateManagerManager
                    .getUpdateManager().UPDATE_INFO_CACHE_FILENAME),
                updInfo);
    }

    public static void toFile(Context ctx, File file, UpdateInfo updInfo) {
        MappingJsonFactory factory;
        JsonGenerator generator;

        factory = new MappingJsonFactory();

        try {
            generator = factory.createJsonGenerator(file, JsonEncoding.UTF8);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return;
        }

        try {
            generator.writeObject(updInfo);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                generator.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
