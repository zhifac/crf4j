package com.github.zhifac.crf4j;

import org.junit.Test;

import java.net.URL;

/**
 * Created by zhifac on 2017/4/3.
 */
public class TestCrfLearn {
    public void testLearnModel(String category, String[] option) {
        URL templ = this.getClass().getClassLoader().getResource("example/" + category + "/template");
        URL train = this.getClass().getClassLoader().getResource("example/" + category + "/train.data");
        assert templ != null;
        assert train != null;
        String[] args = new String[]{templ.getPath(), train.getPath(), category + ".m"};
        if (option != null) {
            String[] newargs = new String[option.length + args.length];
            for (int i = 0; i < option.length; i++) {
                newargs[i] = option[i];
            }
            for (int i = 0; i < args.length; i++) {
                newargs[option.length + i] = args[i];
            }
            args = newargs;
        }
        assert CrfLearn.run(args);
    }

    @Test
    public void testBasicLearnSegModel() {
        testLearnModel("seg", null);
    }

    @Test
    public void testBasicLearnNPModel() {
        testLearnModel("basenp", null);
    }

    @Test
    public void testMiraLearnSegModel() {
        String[] options = {"-a", "MIRA"};
        testLearnModel("seg", options);
    }

    @Test
    public void testCRFL1LearnSegModel() {
        String[] options = {"-a", "CRF-L1"};
        testLearnModel("seg", options);
    }

    @Test
    public void testParamLearnSegModel() {
        String[] options = {"-f", "3", "-c", "4.0"};
        testLearnModel("seg", options);
    }
}
