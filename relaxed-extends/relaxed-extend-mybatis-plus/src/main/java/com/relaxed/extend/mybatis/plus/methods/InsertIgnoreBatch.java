package com.relaxed.extend.mybatis.plus.methods;

/**
 * @author Yakir
 * @Topic InsertIgnoreBatch
 * @Description
 * @date 2021/7/11 10:48
 * @Version 1.0
 */
public class InsertIgnoreBatch extends AbstractInsertBatch{
    private static final String SQL_METHOD = "insertIgnoreBatch";
    /**
     * 插入如果中已经存在相同的记录，则忽略当前新数据
     */
    private static final String INSERT_IGNORE_SQL ="<script>\\nINSERT IGNORE INTO %s %s VALUES %s\\n</script>";


    @Override
    protected String getSql() {
        return INSERT_IGNORE_SQL;
    }

    @Override
    protected String getId() {
        return SQL_METHOD;
    }
}
