package com.pat.api.entity.ext;

import com.pat.api.entity.PatError;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.beans.Transient;
import java.util.List;

/**
 * AibkErrorExt
 *
 * @author chouway
 * @date 2021.05.14
 */
@Data
@EqualsAndHashCode(callSuper=false)
@Accessors(chain = true)
public class PatErrorExt extends PatError {

    private List<StackTraceElement> stackTraceElements;

    @Transient
    public List<StackTraceElement> getStackTraceElements() {
        return stackTraceElements;
    }
}
