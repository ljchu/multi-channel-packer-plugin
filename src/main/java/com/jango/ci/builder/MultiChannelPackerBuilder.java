package com.jango.ci.builder;

import hudson.EnvVars;
import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractDescribableImpl;
import hudson.model.BuildListener;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;

import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

import com.jango.ci.util.CollectionUtil;
import com.jango.ci.util.FileCopy;
import com.jango.ci.util.ModifyXml;
import com.jango.ci.util.ReplaceStrInFile;
import com.jango.ci.util.EnvResolver;

/**
 * 
 * @author Jango Chu
 * 
 */
public class MultiChannelPackerBuilder extends Builder {
	public final String filePath;
	public final String stringToFind;
	public final String xmlNodeName;
	public final String xmlAttributeName;
	public final String xmlAttributeValue;
	public final String newValue;
	public final String choice;
	static HashMap<String, Integer> choiceList = new HashMap<String, Integer>();
	static {
		choiceList
				.put("Replace:Replace a String find in Source File,with the Target Value",
						1);
		choiceList
				.put("XML:Modify the Node's text which Element-name match the input",
						2);
		choiceList
				.put("XML:Modify the Attribute's Value which Element-name and Attribute-name match the input",
						3);
		choiceList
				.put("XML:Modify the Attribute's Value which Element-name,Attribute-name and Attribute-Value match the input",
						4);
		choiceList
				.put("XML:Modify the Node's text which Element-name,Attribute-name and Attribute-Value match the input",
						5);
		choiceList.put(
				"Copy:Copy from the Source File Path to the Target Value", 6);
	}

	@DataBoundConstructor
	public MultiChannelPackerBuilder(String filePath, String stringToFind,
			String xmlNodeName, String xmlAttributeName,
			String xmlAttributeValue, String newValue, String choice) {
		this.filePath = filePath;
		this.stringToFind = stringToFind;
		this.xmlNodeName = xmlNodeName;
		this.xmlAttributeName = xmlAttributeName;
		this.xmlAttributeValue = xmlAttributeValue;
		this.newValue = newValue;
		this.choice = choice;
	}

	public String getFilePath() {
		return filePath;
	}

	public String getChoice() {
		return choice;
	}

	public String getStringToFind() {
		return stringToFind;
	}

	public String getXmlNodeName() {
		return xmlNodeName;
	}

	public String getXmlAttributeName() {
		return xmlAttributeName;
	}

	public String getXmlAttributeValue() {
		return xmlAttributeValue;
	}

	public String getNewValue() {
		return newValue;
	}

	@Override
	public boolean perform(AbstractBuild<?, ?> build, Launcher launcher,
			BuildListener listener) {
		if (filePath.equals(null) || filePath.equals("")) {
			listener.getLogger().println(
					"[ERROR]:Source File Path should not be empty！");
			return false;
		}
		EnvVars envVars = new EnvVars();
		try {
			envVars = build.getEnvironment(listener);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		String filePathChanged = EnvResolver.changeStringWithEnv(envVars,
				filePath);
		String newValueChanged = EnvResolver.changeStringWithEnv(envVars,
				newValue);
		String stringToFindChanged = EnvResolver.changeStringWithEnv(envVars,
				stringToFind);
		String xmlNodeNameChanged = EnvResolver.changeStringWithEnv(envVars,
				xmlNodeName);
		String xmlAttributeNameChanged = EnvResolver.changeStringWithEnv(
				envVars, xmlAttributeName);
		String xmlAttributeValueChanged = EnvResolver.changeStringWithEnv(
				envVars, xmlAttributeValue);

		boolean result = false;
		switch (choiceList.get(choice)) {
		case 1: {
			if (!new File(filePathChanged).exists()) {
				listener.getLogger().println(
						"[ERROR]:Sourece file\"" + filePathChanged
								+ "\" dose not exist.");
				return false;
			}
			listener.getLogger().println(
					"[INFO]:"
							+ CollectionUtil.getKeyOfMapByValue(choiceList, 1));
			ReplaceStrInFile re = new ReplaceStrInFile();
			if (stringToFindChanged.equals(null)
					|| stringToFindChanged.equals("")) {
				listener.getLogger().println(
						"[ERROR]:String to find should not be empty！");
				return false;
			}
			result = re.replaceInFile(listener, filePathChanged,
					stringToFindChanged, newValueChanged);
			if (result) {
				listener.getLogger().println(
						"[INFO]:Finished replace the \"" + stringToFindChanged
								+ "\" with \"" + newValueChanged
								+ "\" in file:" + filePathChanged);
			} else {
				listener.getLogger().println("[ERROR]:Fail to replace");
			}
			return result;
		}
		case 2: {
			if (!new File(filePathChanged).exists()) {
				listener.getLogger().println(
						"[ERROR]:Sourece file\"" + filePathChanged
								+ "\" dose not exist.");
				return false;
			}
			listener.getLogger().println(
					"[INFO]:"
							+ CollectionUtil.getKeyOfMapByValue(choiceList, 2));
			if (xmlNodeNameChanged.equals(null)
					|| xmlNodeNameChanged.equals("")) {
				listener.getLogger().println(
						"[ERROR]:The name of the element should not be empty");
				return false;
			}
			result = ModifyXml.modifyNodeTextByTagName(listener,
					filePathChanged, xmlNodeNameChanged, newValueChanged);
			if (result) {
				listener.getLogger().println(
						"[INFO]:Finished modify the text of the element \""
								+ xmlNodeNameChanged + "\" with\""
								+ newValueChanged + "\" in the xml file:"
								+ filePathChanged);
			} else {
				listener.getLogger().println(
						"[ERROR]:Fail to modify the xml file..");
			}
			return result;
		}
		case 3: {
			if (!new File(filePathChanged).exists()) {
				listener.getLogger().println(
						"[ERROR]:Sourece file\"" + filePathChanged
								+ "\" dose not exist.");
				return false;
			}
			listener.getLogger().println(
					"[INFO]:"
							+ CollectionUtil.getKeyOfMapByValue(choiceList, 3));
			if (xmlNodeNameChanged.equals(null)
					|| xmlNodeNameChanged.equals("")
					|| xmlAttributeNameChanged.equals(null)
					|| xmlAttributeNameChanged.equals("")) {
				listener.getLogger()
						.println(
								"[ERROR]:The name of the element and the attribute should not be empty");
				return false;
			}
			result = ModifyXml.modifyAttributeValueByTagNameAndAttribute(
					listener, filePathChanged, xmlNodeNameChanged,
					xmlAttributeNameChanged, newValueChanged);
			if (result) {
				listener.getLogger().println(
						"[INFO]:Finished modify the value of the attribute \""
								+ xmlAttributeNameChanged + "\" with\""
								+ newValueChanged + "\" in the xml file:"
								+ filePathChanged);
			} else {
				listener.getLogger().println(
						"[ERROR]:Fail to modify the xml file..");
			}
			return result;
		}
		case 4: {
			if (!new File(filePathChanged).exists()) {
				listener.getLogger().println(
						"[ERROR]:Sourece file\"" + filePathChanged
								+ "\" dose not exist.");
				return false;
			}
			listener.getLogger().println(
					"[INFO]:"
							+ CollectionUtil.getKeyOfMapByValue(choiceList, 4));
			if (xmlNodeNameChanged.equals(null)
					|| xmlNodeNameChanged.equals("")
					|| xmlAttributeNameChanged.equals(null)
					|| xmlAttributeNameChanged.equals("")
					|| xmlAttributeValueChanged.equals(null)
					|| xmlAttributeValueChanged.equals("")) {
				listener.getLogger()
						.println(
								"[ERROR]:The name of the element and the attribute ,the value of the attribute should not be empty");
				return false;
			}
			result = ModifyXml
					.modifyAttributeValueByTagNameAndAttributeAndAttributeValue(
							listener, filePathChanged, xmlNodeNameChanged,
							xmlAttributeNameChanged, xmlAttributeValueChanged,
							newValueChanged);
			if (result) {
				listener.getLogger().println(
						"[INFO]:Finished modify the value of the attribute \""
								+ xmlAttributeNameChanged + "\" with\""
								+ newValueChanged + "\" in the xml file:"
								+ filePathChanged);
			} else {
				listener.getLogger().println(
						"[ERROR]:Fail to modify the xml file..");
			}
			return result;
		}
		case 5: {
			if (!new File(filePathChanged).exists()) {
				listener.getLogger().println(
						"[ERROR]:Sourece file\"" + filePathChanged
								+ "\" dose not exist.");
				return false;
			}
			listener.getLogger().println(
					"[INFO]:"
							+ CollectionUtil.getKeyOfMapByValue(choiceList, 5));
			if (xmlNodeNameChanged.equals(null)
					|| xmlNodeNameChanged.equals("")
					|| xmlAttributeNameChanged.equals(null)
					|| xmlAttributeNameChanged.equals("")
					|| xmlAttributeValueChanged.equals(null)
					|| xmlAttributeValueChanged.equals("")) {
				listener.getLogger()
						.println(
								"[ERROR]:The name of the element and the attribute ,the value of the attribute should not be empty");
				return false;
			}
			result = ModifyXml
					.modifyNodeValueByTagNameAndAttributeAndAttributeValue(
							listener, filePathChanged, xmlNodeNameChanged,
							xmlAttributeNameChanged, xmlAttributeValueChanged,
							newValueChanged);
			if (result) {
				listener.getLogger().println(
						"[INFO]:Finished modify the text of the element \""
								+ xmlNodeNameChanged + "\" with\""
								+ newValueChanged + "\" in the xml file:"
								+ filePathChanged);
			} else {
				listener.getLogger().println(
						"[ERROR]:Fail to modify the xml file..");
			}
			return result;
		}
		case 6: {
			listener.getLogger().println(
					"[INFO]:"
							+ CollectionUtil.getKeyOfMapByValue(choiceList, 6));
			if (!new File(filePathChanged).exists()) {
				listener.getLogger().println(
						"[ERROR]:Path \"" + filePathChanged
								+ "\" dose not exist.");
				return false;
			}
			FileCopy fileCopy = new FileCopy();
			result = fileCopy.copyFile(listener, filePathChanged,
					newValueChanged);
			if (result) {
				listener.getLogger().println(
						"[INFO]:Finished copy" + filePathChanged + " to"
								+ newValueChanged + ".");
			} else {
				listener.getLogger().println("[ERROR]:Copy file failure.");
			}
			return result;
		}
		default:
			break;
		}
		return result;
	}

	@Override
	public DescriptorImpl getDescriptor() {
		return (DescriptorImpl) super.getDescriptor();
	}

	@Extension
	public static final class DescriptorImpl extends
			BuildStepDescriptor<Builder> {
		public DescriptorImpl() {
			load();
		}

		public FormValidation doCheckFilePath(@QueryParameter String value)
				throws IOException, ServletException {
			if (value.length() == 0)
				return FormValidation
						.error("Please select a text file,of an XML file,of a folder for copy.");
			if (!(new File(value).exists()))
				return FormValidation.error("File\"" + value
						+ "\" dose not exist,please check it.");
			return FormValidation.ok();
		}

		public FormValidation doCheckNewValue(@QueryParameter String value)
				throws IOException, ServletException {
			if (value.length() == 0)
				return FormValidation.error("Please set this value.");
			return FormValidation.ok();
		}

		public FormValidation doCheckXmlAttributeName(
				@QueryParameter String value) throws IOException,
				ServletException {
			if (value.length() == 0)
				return FormValidation
						.warning("Fill this with name of an attribute in the XML file,if required.");
			return FormValidation.ok();
		}

		public FormValidation doCheckXmlNodeName(@QueryParameter String value)
				throws IOException, ServletException {
			if (value.length() == 0)
				return FormValidation
						.warning("Fill this with a tag name in the XML file,if required.");
			return FormValidation.ok();
		}

		public FormValidation doCheckXmlAttributeValue(
				@QueryParameter String value) throws IOException,
				ServletException {
			if (value.length() == 0)
				return FormValidation
						.warning("Fill this with the value of an attribute in the XML file,if required.");
			return FormValidation.ok();
		}

		public FormValidation doCheckStringToFind(@QueryParameter String value)
				throws IOException, ServletException {
			if (value.length() == 0)
				return FormValidation.warning("Fill this with a string in the file,if required.");
			return FormValidation.ok();
		}

		public boolean isApplicable(
				@SuppressWarnings("rawtypes") Class<? extends AbstractProject> aClass) {
			return true;
		}

		@Override
		public String getDisplayName() {
			return "Invoke multi-channel Packer";
		}

		public ListBoxModel doFillChoiceItems() {
			ListBoxModel aBoxModel = new ListBoxModel();
			for (int i = 0; i < choiceList.size(); i++) {
				aBoxModel.add(CollectionUtil.getKeyOfMapByValue(choiceList,
						i + 1));
			}
			return aBoxModel;
		}

		@Override
		public boolean configure(StaplerRequest req, JSONObject formData)
				throws FormException {
			return super.configure(req, formData);
		}

	}

	public static abstract class Entry extends AbstractDescribableImpl<Entry> {
	}
}
