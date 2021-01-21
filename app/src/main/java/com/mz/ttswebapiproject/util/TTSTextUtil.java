package com.mz.ttswebapiproject.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author 作者：mazhuang
 * @Date 创建时间：2021/1/20 15:22
 * @Description 文件描述：
 */
public class TTSTextUtil {
    /**
     * 句子结束符.
     */
    private static final String SEPARATOR_REGEX = "[,.?!，。？！;；]";

    public static String[] splitSentence(String sentence) {
        //1. 定义匹配模式
        Pattern p = Pattern.compile(SEPARATOR_REGEX);
        Matcher m = p.matcher(sentence);

        //2. 拆分句子[拆分后的句子符号也没了]
        String[] words = p.split(sentence);

        //3. 保留原来的分隔符
        if (words.length > 0) {
            int count = 0;
            while (count < words.length) {
                if (m.find()) {
                    words[count] += m.group();
                }
                count++;
            }
        }
        return words;
    }
    public List limitNumber(String[] words) {
        //1. 存储限制字数的数据
        List<String> wordList = new ArrayList<>();

        //2. 限制字数在10以内
        int wordsLength = words.length;
        for (int i = 0; i < wordsLength; i++) {
            // 循环获取拆分后的每个句子
            String word = words[i];

            // 每个句子的长度
            int length = word.length();
            System.out.println("word = " + word + "-------------->length = " + length);

            // 当字数>=10，直接存储到wordList
            if (length >= 500) {
                wordList.add(word);
            } else {
                // 防止ArrayIndexOutOfBoundsException
                if (i + 1 >= wordsLength) {
                    wordList.add(word);
                    return wordList;
                }

                // 获取下一个句子的长度
                String nextWord = words[i + 1];
                int nextLength = nextWord.length();

                // 如果上一个句子的长度 + 下一个句子的长度 <= 10,那么拼接成一个句子
                int totalLength = length + nextLength;
                if (totalLength <= 10) {
                    wordList.add(word + nextWord);
                    i++;
                } else {
                    wordList.add(word);
                }
            }
        }
        return wordList;
    }
}
