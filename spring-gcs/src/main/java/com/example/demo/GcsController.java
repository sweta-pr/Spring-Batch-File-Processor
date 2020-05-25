package com.example.demo;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.WritableResource;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
public class GcsController{

    private static final Logger logger = LoggerFactory.getLogger(GcsController.class);
    //gs://bucket-name/foldername/filename
    @Value("gs://inbound-file-processor/patient/10985_8471_TJSamson_202001_Patient_1_2020.txt")
    private Resource gcsFile;

    @Autowired
    private Environment environment;

    @Value("${path}")
    private String home;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String readGcsFile() throws IOException {
        System.out.println("Hello" + home);
        logger.info("{}", environment.getProperty("JAVA_HOME"));
        logger.info("{}", environment.getProperty("app.name"));
     //   System.out.println("Hello" + env.getProperty("GOOGLE_APPLICATION_CREDENTIALS"));
        System.out.println("Hello" + environment.getProperty("GOOGLE_APPLICATION_CREDENTIALS"));
        return StreamUtils.copyToString(
                gcsFile.getInputStream(),
                Charset.defaultCharset()) + "\n";
    }

    //@RequestMapping(value = "/write", method = RequestMethod.POST)
    @PostMapping("/patient")
    @ResponseBody
    String writeGcs(@RequestParam String data) throws IOException {
        try (OutputStream os = ((WritableResource) gcsFile).getOutputStream()) {
            os.write(data.getBytes());
        }
        return "file was updated\n";
    }
}