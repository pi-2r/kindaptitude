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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zen on 16/06/17.
 */
@Service
public class ThrowawayDomains {
    private Set<String> dict;
    final String regex = "(?<=@)[a-zA-Z0-9\\.]+(?<=)";
    final Pattern pattern = Pattern.compile(regex);

    private ThrowawayDomains(){
        try {
            String dictPath = getClass().getClassLoader().getResource("throwaway_domains.txt").getFile();
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

    private static ThrowawayDomains throwawayDomains = new ThrowawayDomains();

    public boolean isThrowawayDomains(String domainEmail){
        boolean flag=false;
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(domainEmail);

        while (matcher.find()) {
            if(this.dict.contains(matcher.group(0).trim().toLowerCase())){
                flag=true;
            }
        }
        return flag;
    }
    public static ThrowawayDomains getInstance(){
        return throwawayDomains;
    }
    public Set<String> getDict(){
        return this.dict;
    }
}
