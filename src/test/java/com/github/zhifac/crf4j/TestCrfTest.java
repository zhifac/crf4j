package com.github.zhifac.crf4j;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Date;

/**
 * Created by zhifac on 2017/4/3.
 */
public class TestCrfTest {
    public void testModel(String category, double checkThreshold, String testFile,
                          boolean retrain, String[] trainArgs, String[] testArgs) {
        File f = new File(category + ".m");
        if (retrain || !f.exists()) {
            new TestCrfLearn().testLearnModel(category, trainArgs);
        }
        assert f.exists();
        File temp = null;
        try {
            temp = File.createTempFile("crftmp-" + category + new Date().getTime(), ".out");
            temp.deleteOnExit();
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
        URL test = this.getClass().getClassLoader().getResource("example/" + category + "/" + testFile);
        assert test != null;
        String[] args = {"-m", category + ".m", test.getPath(), "-o", temp.getAbsolutePath()};
        if (testArgs != null) {
            String[] newargs = new String[testArgs.length + args.length];
            for (int i = 0; i < testArgs.length; i++) {
                newargs[i] = testArgs[i];
            }
            for (int i = 0; i < args.length; i++) {
                newargs[testArgs.length + i] = args[i];
            }
            args = newargs;
        }
        assert CrfTest.run(args);
        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(temp), "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            int total = 0;
            int correct = 0;
            while((line = br.readLine()) != null) {
                if (line.length() == 0 || line.charAt(0) == '#' || line.charAt(0) == ' ') {
                    continue;
                } else {
                    String[] toks = line.split("[\t ]", -1);
                    assert toks.length > 2;
                    if (toks[toks.length - 1].equals(toks[toks.length - 2])) {
                        correct++;
                    }
                    total++;
                }
            }
            br.close();
            double score = (double)correct / total;
            assert score >= checkThreshold;
        }catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testBasicSegModel() {
        testModel("seg", 1.0, "train.data", true, null, null);
    }

    @Test
    public void testSegTestdata() {
        testModel("seg", 0.7, "test.data", false, null, null);
    }

    @Test
    public void testFeatureFreqSegTestdata() {
        String[] trainopts = {"-f", "3", "-c", "4.0"};
        testModel("seg", 0.7, "test.data", true, trainopts, null);
    }

    @Test
    public void testBasicNPModel() {
        testModel("basenp", 1.0, "train.data", true, null, null);
    }

    @Test
    public void testNPTestdata() {
        testModel("basenp", 0.7, "test.data", false, null, null);
    }
}
