package com.assignment.model_utils.model_runner_factory;

import com.assignment.model_utils.model_runner.ClipModelRunner;
import com.assignment.model_utils.model_runner.ModelRunner;
import com.assignment.model_utils.model_runner.ViTModelRunner;

public class ModelRunnerFactory {
    public static ModelRunner create(String type) {
        if ("vit".equals(type))
            return (new ViTModelRunner());
        else if ("clip".equals(type))
            return (new ClipModelRunner());
        else
            return (null);
    }
}
