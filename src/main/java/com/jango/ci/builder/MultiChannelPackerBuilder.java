package com.jango.ci.builder;

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
import com.jango.ci.util.ModifyXml;
import com.jango.ci.util.ReplaceStrInFile;

/**
 * 
 * @author Jango Chu
 * 
 */
public class MultiChannelPackerBuilder extends Builder {
	public final String filePath;
	public final String stringToFind;// 查找字符串
	public final String xmlNodeName;// 节点名称
	public final String xmlAttributeName;// 属性名称
	public final String xmlAttributeValue;// 属性值
	public final String newValue;
	public final String choice;
	static HashMap<String, Integer> choiceList = new HashMap<String, Integer>();
	{
		choiceList.put("文本替换", 1);
		choiceList.put("根据节点名称，修改节点文本", 2);
		choiceList.put("根据节点名称、属性名称，修改属性值", 3);
		choiceList.put("根据节点名称、属性名称、属性值，修改属性值", 4);
		choiceList.put("根据节点名称、属性名称、属性值，修改节点文本", 5);

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

	@Override
	public boolean perform(AbstractBuild<?, ?> build, Launcher launcher,
			BuildListener listener) {
		boolean result = false;
		ModifyXml modifyXml = new ModifyXml();
		switch (choiceList.get(choice)) {
		case 1: {
			ReplaceStrInFile re = new ReplaceStrInFile();
			result = re.replaceInFile(filePath, stringToFind, newValue);

			if (result) {
				listener.getLogger().println(
						"[INFO]:完成文件替换在" + filePath + "文件中，将" + stringToFind
								+ "字符串替换成了" + newValue + "！");
				return true;
			} else {
				listener.getLogger().println(
						"[ERROR]:文件字符串替换出现错误，请检查被修改的文件是否存在及其读写权限！");
				return false;
			}
		}
		case 2: {

			result = modifyXml.modifyNodeTextByTagName(filePath, xmlNodeName,
					newValue);
			if (result) {
				listener.getLogger().println(
						"[INFO]:完成节点文本修改在" + filePath + "文件中，将" + xmlNodeName
								+ "的文本修改成了了" + newValue + "！");
				return true;
			} else {
				listener.getLogger().println(
						"[ERROR]:节点文本修改出现错误，请检查被修改的文件是否存在及其读写权限！");
				return false;
			}
		}
		case 3: {
			result = modifyXml.modifyAttributeValueByTagNameAndAttribute(
					filePath, xmlNodeName, xmlAttributeName, newValue);
			if (result) {
				listener.getLogger().println(
						"[INFO]:完成节点文本修改在" + filePath + "文件中，将" + xmlNodeName
								+ "的文本修改成了了" + newValue + "！");
				return true;
			} else {
				listener.getLogger().println(
						"[ERROR]:节点文本修改出现错误，请检查被修改的文件是否存在及其读写权限！");
				return false;
			}
		}
		case 4: {
			result = modifyXml
					.modifyAttributeValueByTagNameAndAttributeAndAttributeValue(
							filePath, xmlNodeName, xmlAttributeName,
							xmlAttributeValue, newValue);

			if (result) {
				listener.getLogger().println(
						"[INFO]:完成节点文本修改在" + filePath + "文件中，将" + xmlNodeName
								+ "的文本修改成了了" + newValue + "！");
				return true;
			} else {
				listener.getLogger().println(
						"[ERROR]:节点文本修改出现错误，请检查被修改的文件是否存在及其读写权限！");
				return false;
			}
		}
		case 5: {
			result = modifyXml
					.modifyNodeValueByTagNameAndAttributeAndAttributeValue(
							filePath, xmlNodeName, xmlAttributeName,
							xmlAttributeValue, newValue);
			if (result) {
				listener.getLogger().println(
						"[INFO]:完成节点文本修改在" + filePath + "文件中，将" + xmlNodeName
								+ "的文本修改成了了" + newValue + "！");
				return true;
			} else {
				listener.getLogger().println(
						"[ERROR]:节点文本修改出现错误，请检查被修改的文件是否存在及其读写权限！");
				return false;
			}
		}
		default:
			break;
		}

		return false;
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
				return FormValidation.error("请选择一个文件！");
			if (!(new File(value).exists()))
				return FormValidation.error("文件\"" + value + "\" 不存在，请检查.");
			return FormValidation.ok();
		}

		public FormValidation doCheckNewValue(@QueryParameter String value)
				throws IOException, ServletException {
			if (value.length() == 0)
				return FormValidation.error("请填需要修改的值.");
			return FormValidation.ok();
		}

		public FormValidation doCheckXmlAttributeName(
				@QueryParameter String value) throws IOException,
				ServletException {
			if (value.length() == 0)
				return FormValidation.warning("根据选择，填写属性名称.");
			return FormValidation.ok();
		}

		public FormValidation doCheckXmlNodeName(@QueryParameter String value)
				throws IOException, ServletException {
			if (value.length() == 0)
				return FormValidation.warning("除非是文本替换，否则，请填写此节点名称.");
			return FormValidation.ok();
		}

		public FormValidation doCheckXmlAttributeValue(
				@QueryParameter String value) throws IOException,
				ServletException {
			if (value.length() == 0)
				return FormValidation.warning("根据选择，填写属性值，作为xml查找依据.");
			return FormValidation.ok();
		}

		public boolean isApplicable(Class<? extends AbstractProject> aClass) {
			return true;
		}

		@Override
		public String getDisplayName() {
			return "Android Multiple Package.";
		}

		public ListBoxModel doFillChoiceItems() {
			return new ListBoxModel().add("文本替换").add("根据节点名称，修改节点文本")
					.add("根据节点名称、属性名称，修改属性值").add("根据节点名称、属性名称、属性值，修改属性值")
					.add("根据节点名称、属性名称、属性值，修改节点文本");
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
