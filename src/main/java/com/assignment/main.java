package com.assignment;

import com.assignment.model_utils.model_runner.ClipInput;
import com.assignment.model_utils.model_runner.ModelRunner;
import com.assignment.model_utils.model_runner.VLMInput;
import com.assignment.model_utils.model_runner_factory.ModelRunnerFactory;
import com.assignment.outputs.output_interpreter.OutputInterpreter;
import com.assignment.outputs.output_interpreter.VLMOutputInterpreter;
import com.assignment.outputs.output_interpreter_factory.OutputInterpreterFactory;
import com.assignment.preprocessor_utils.preprocessors.Preprocessors;
import com.assignment.preprocessor_utils.preprocessorsFactory.PreprocessorsFactory;
import com.assignment.utils.tokenizerUtils.ClipTokenizer;
import com.assignment.utils.tokenizerUtils.VLMTokenizer;

import java.util.Scanner;

public class main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        float[] data;
        float[][] output;

        System.out.print("Enter desired model: ");
        String type = scanner.nextLine();

        Preprocessors preprocessor = PreprocessorsFactory.create(type);
        data = preprocessor.preprocess();

        ModelRunner model = ModelRunnerFactory.create(type);

        if ("clip".equals(type)) {
            System.out.print("Enter text to compare with the image: ");

            String text = scanner.nextLine();

            try {
                ClipTokenizer tokenizer = new ClipTokenizer();
                long[] textTokenIds = tokenizer.tokenize(text);
                ClipInput clipInput = new ClipInput(data, textTokenIds);
                output = ((ModelRunner<ClipInput>) model).modelRunner(clipInput);

            } catch (Exception e) {
                System.out.println("Error running CLIP! :(");
                e.printStackTrace();
                scanner.close();
                return;
            }

        } else if ("vlm".equals(type)) {
            System.out.print("Enter your question about the image: ");

            String question = scanner.nextLine();

            try {
                VLMTokenizer tokenizer = new VLMTokenizer();

                String prompt = "<|im_start|>User:<image>" + question + "<end_of_utterance>Assistant:";
                long[] inputIds = tokenizer.tokenize(prompt);

                VLMInput vlmInput = new VLMInput(data, inputIds);
                output = ((ModelRunner<VLMInput>) model).modelRunner(vlmInput);

                VLMOutputInterpreter interpreter = new VLMOutputInterpreter();
                interpreter.interpret(output);

            } catch (Exception e) {
                System.out.println("Error running VLM! :(");
                e.printStackTrace();
            }

            scanner.close();
            return;

        } else {
            output = model.modelRunner(data);
        }

        OutputInterpreter interpreter = OutputInterpreterFactory.create(type);
        interpreter.interpret(output);

        scanner.close();
    }
}