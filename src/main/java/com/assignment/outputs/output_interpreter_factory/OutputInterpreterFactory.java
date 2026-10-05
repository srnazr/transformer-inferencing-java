package com.assignment.outputs.output_interpreter_factory;

import com.assignment.outputs.output_interpreter.ClipInterpreter;
import com.assignment.outputs.output_interpreter.OutputInterpreter;
import com.assignment.outputs.output_interpreter.ViTInterpreter;

import java.nio.charset.StandardCharsets;

public class OutputInterpreterFactory {
    public static OutputInterpreter create(String type) {
        if ("vit".equals(type))
            return (new ViTInterpreter());
        else if ("clip".equals(type))
            return (new ClipInterpreter());
        else
            return null;
    }
}
