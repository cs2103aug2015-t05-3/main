# A0076510Mreused
###### storage\CommandFileHandler.java
``` java
	private boolean fileCopyFromResource(String newFileStr) {
		assert newFileStr != null;
		assert !newFileStr.isEmpty();
		
		File newFile = new File(newFileStr);
		newFile.delete();

		InputStream inputStream = this.getClass().getClassLoader()
				.getResourceAsStream(_commandResource);
		
		OutputStream outputStream = null;
			try {
				outputStream = new FileOutputStream(newFileStr);
				int read = 0;
				byte[] bytes = new byte[BYTE_ARRAY_NUMBER];
				while ((read = inputStream.read(bytes)) != EOF_NUMBER) {
					outputStream.write(bytes, OFFSET_NUMBER, read);
				}
				inputStream.close();
				outputStream.close();
				LogHandler.getLog().log(Level.INFO, MSG_COPYCOMMANDFILE);
				return true;
			} catch (FileNotFoundException e) {
				LogHandler.getLog().log(Level.SEVERE, 
						String.format(EXCEPTION_FILENOTFOUND, e));
				return false;
			} catch (IOException e) {
				LogHandler.getLog().log(Level.SEVERE, 
						String.format(EXCEPTION_IO, e));
				return false;
			}
	}
```
###### storage\TaskFileHandler.java
``` java
	/**
	 * Remove text nodes that are used for indentation.
	 * 
	 * @param node 
	 * 		A single node in XML file
	 */
	private void removeEmptyText(Node node) {
		Node child = node.getFirstChild();
		while (child != null) {
			Node sibling = child.getNextSibling();
			if (child.getNodeType() == Node.TEXT_NODE) {
				if (child.getTextContent().trim().isEmpty()) {
					node.removeChild(child);
				}
			} else {
				removeEmptyText(child);
			}
			child = sibling;
		}
	}

```
###### test\IntTest.java
``` java
	public String getCheckSum(String file) {

		StringBuffer sb1 = null;

		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			FileInputStream fis = new FileInputStream(file);

			byte[] dataBytes = new byte[1024];

			int nread = 0;
			while ((nread = fis.read(dataBytes)) != -1) {
				md.update(dataBytes, 0, nread);
			}
			;
			byte[] mdbytes = md.digest();
			sb1 = new StringBuffer();
			for (int i = 0; i < mdbytes.length; i++) {
				sb1.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			fis.close();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return sb1.toString();
	}

}
```
###### test\StorageTest.java
``` java
	public String getCheckSum(String file) {
		
		StringBuffer sb1 = null;
		
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			FileInputStream fis = new FileInputStream(file);

			byte[] dataBytes = new byte[1024];

			int nread = 0;
			while ((nread = fis.read(dataBytes)) != -1) {
				md.update(dataBytes, 0, nread);
			}
			;
			byte[] mdbytes = md.digest();
			sb1 = new StringBuffer();
			for (int i = 0; i < mdbytes.length; i++) {
				sb1.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			fis.close();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return sb1.toString();
	}
	
	public void fileCopy(String toCopy, String newFileStr) {
		File newFile = new File(newFileStr);
		newFile.delete();

		File file = new File(toCopy);
		FileChannel inputChannel = null;
		FileChannel outputChannel = null;

		try {
			FileInputStream fis = new FileInputStream(file);
			inputChannel = fis.getChannel();
			FileOutputStream fos = new FileOutputStream(newFile);
			outputChannel = fos.getChannel();
			outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
			inputChannel.close();
			outputChannel.close();
			fis.close();
			fos.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
```
