//package com.relaxed.common.translation.querier;
//
//import com.relaxed.common.translation.core.TranslationRequest;
//import com.relaxed.common.translation.core.TranslationResponse;
//import com.relaxed.common.translation.core.Translator;
//import com.relaxed.common.translation.enums.LangEnum;
//import com.relaxed.common.translation.trans.google.GoogleRequest;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @author Yakir
// * @Topic Querier
// * @Description 聚合多个翻译器
// * @date 2022/1/11 16:17
// * @Version 1.0
// */
//public class Querier<T extends TranslationRequest> {
//
//    private List<T> collection;
//
//
//    private Translator translator;
//
//
//    public List<String> execute() {
//        List<String> result = new ArrayList<String>();
//
//        for (T element : collection) {
//            TranslationResponse translate = translator.translate(element);
//            if (translate.success()){
//
//            }
//            if (element.getClass().getName().contains("Translator")) {
//                result.add(element.run(from, to, text));
//            } else if (element.getClass().getName().contains("TTS")) {
//                result.add(element.run(from, text));
//            }
//        }
//        return result;
//    }
//
//    public void attach(T element){
//        collection.add(element);
//    }
//
//    public void detach(T element) {
//        collection.remove(element);
//    }
//
//}
