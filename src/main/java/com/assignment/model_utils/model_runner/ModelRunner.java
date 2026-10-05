package com.assignment.model_utils.model_runner;

public interface ModelRunner<T> {
    float[][] modelRunner(T data);
}