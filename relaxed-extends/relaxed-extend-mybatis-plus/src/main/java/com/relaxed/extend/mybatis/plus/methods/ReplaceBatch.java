package com.relaxed.extend.mybatis.plus.methods;

/**
 * @author Yakir
 * @Topic ReplaceBatch
 * @Description
 * @date 2021/7/11 10:50
 * @Version 1.0
 */
public class ReplaceBatch extends AbstractInsertBatch{
    private static final String SQL_METHOD = "replaceBatch";
    /**
     * 表示插入替换数据，需求表中有PrimaryKey，或者unique索引，如果数据库已经存在数据，则用新数据替换，如果没有数据效果则和insert into一样；
     */
    private static final String INSERT_IGNORE_SQL ="\"<script>\\nREPLACE INTO %s %s VALUES %s\\n</script>\"";

    @Override
    protected String getSql() {
        return SQL_METHOD;
    }

    @Override
    protected String getId() {
        return INSERT_IGNORE_SQL;
    }
}
