package com.wwb.commonbase.mybatisplus;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.core.MybatisConfiguration;

/**
 * @author weibo
 */
public class CustomConfigurationCustomizer implements ConfigurationCustomizer {

    @Override
    public void customize(MybatisConfiguration configuration) {
        configuration.setUseDeprecatedExecutor(false);
    }
}