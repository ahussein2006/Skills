package com.code.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import com.code.enums.ErrorMessageCodesEnum;
import com.code.exceptions.BusinessException;

public class IOStreamUtil {

    private IOStreamUtil() {
    }

    public static InputStream getFileInputStream(String filePath) throws BusinessException {
	try {
	    return new FileInputStream(new File(filePath));
	} catch (FileNotFoundException e) {
	    throw new BusinessException(ErrorMessageCodesEnum.FILE_NOT_FOUND);
	}
    }

    public static String getInputStreamString(InputStream inputStream) {
	StringBuilder stringBuilder = new StringBuilder();
	Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8);
	while (scanner.hasNext())
	    stringBuilder.append(scanner.nextLine());
	scanner.close();
	return stringBuilder.toString();
    }
}
