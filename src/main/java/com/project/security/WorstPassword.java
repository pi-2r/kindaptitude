package com.project.security;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableSet;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by zen on 21/05/17.
 */
@Service
public class WorstPassword {
    private Set<String> dict;


    private WorstPassword(){
        try {
            String dictPath = getClass().getClassLoader().getResource("worst-password.txt").getFile();
            Set<String> tmpDict = Files.readLines(new File(dictPath), Charsets.UTF_8,
                    new LineProcessor<Set<String>>() {
                        Set result = new HashSet<String>();
                        public boolean processLine(String line) {
                            result.add(line.trim());
                            return true;
                        }
                        public Set<String> getResult(){
                            return result;
                        }
                    });
            this.dict = ImmutableSet.copyOf(tmpDict);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private static WorstPassword worstPassword = new WorstPassword();

    public boolean isWorstPassword(String password){
        return this.dict.contains(password);
    }
    public static WorstPassword getInstance(){
        return worstPassword;
    }
    public Set<String> getDict(){
        return this.dict;
    }

}