package com.leyou.goods.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Map;

@Service
public class GoodsHtmlService {

    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private GoodsService goodsService;

    private static final Logger LOGGER = LoggerFactory.getLogger(GoodsHtmlService.class);


    public void createHtml(Long spuId){
        Context context = new Context();
        Map<String, Object> spuMap = this.goodsService.loadData(spuId);
        context.setVariables(spuMap);
        PrintWriter printWriter=null;
        try {
            File file=new File("C:\\Users\\Administrator\\Downloads\\nginx-1.19.6\\nginx-1.19.6\\html\\item\\"+spuId+".html");

             printWriter = new PrintWriter(file);

            this.templateEngine.process("item",context,printWriter);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            if(printWriter!=null){
                printWriter.close();
            }
        }

    }

    public void deleteHtml(Long id) {
        File file = new File("C:\\Users\\Administrator\\Downloads\\nginx-1.19.6\\nginx-1.19.6\\html\\item\\" + id + ".html");
        file.deleteOnExit();

    }
}
