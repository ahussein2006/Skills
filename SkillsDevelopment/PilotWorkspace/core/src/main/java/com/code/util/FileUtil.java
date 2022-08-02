package com.code.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.code.enums.ErrorMessageCodesEnum;
import com.code.exceptions.BusinessException;

public class FileUtil {

    private FileUtil() {
    }

    public static InputStream getFileInputStream(String filePath) throws BusinessException {
	try {
	    return new FileInputStream(new File(filePath));
	} catch (FileNotFoundException e) {
	    throw new BusinessException(ErrorMessageCodesEnum.FILE_NOT_FOUND.getValue());
	}
    }
}
