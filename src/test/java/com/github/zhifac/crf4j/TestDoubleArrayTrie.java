package com.github.zhifac.crf4j;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Created by zhifac on 2017/4/3.
 */
public class TestDoubleArrayTrie {
    @Test
    public void testDAT() {
        List<String> strs = Arrays.asList("一举", "一举成名", "一举成名天下知");
        int[] value = new int[]{3, 400, 5};
        DoubleArrayTrie dat = new DoubleArrayTrie();
        dat.build(strs, null, value, strs.size());
        assert dat.exactMatchSearch("xxx") == -1;
        assert dat.exactMatchSearch("一举") == 3;
        assert dat.exactMatchSearch("一举成名") == 400;
        dat.setKey(null);
        dat.setValue(null);
        dat.recoverKeyValue();
        assert dat.getKey().size() == 3;
        assert dat.getValue().length == 3;
        assert dat.getKey().get(0).equals("一举");
        assert dat.getKey().get(1).equals("一举成名");
        assert dat.getKey().get(2).equals("一举成名天下知");
        assert dat.getValue()[0] == 3;
        assert dat.getValue()[1] == 400;
        assert dat.getValue()[2] == 5;
    }
}
