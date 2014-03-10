package com.jango.ci.builder;

import java.io.IOException;
import java.io.File;

import javax.servlet.ServletException;

import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import hudson.util.FormValidation;

import com.jango.ci.util.ReplaceStrInFile;
/**
 * Sample {@link Builder}.
 *
 * <p>
 * When the user configures the project and enables this builder,
 * {@link DescriptorImpl#newInstance(StaplerRequest)} is invoked
 * and a new {@link MultiChannelPackerBuilder} is created. The created
 * instance is persisted to the project configuration XML by using
 * XStream, so this allows you to use instance fields (like {@link #filePath})
 * to remember the configuration.
 *
 * <p>
 * When a build is performed, the {@link #perform(AbstractBuild, Launcher, BuildListener)}
 * method will be invoked. 
 *
 * @author Jango Chu
 */

public class MultiChannelPackerBuilder extends Builder{
	public final String filePath;
	public final String stringToFind;
	public final String stringToReplace;
	public final String xmlNodeName;
	public final String newNodeNameString;
	
//	@DataBoundConstructor
//	public MultiChannelPackerBuilder(String filePath,String stringToFind,String stringToReplace) {
//        this.filePath = filePath;
//        this.stringToFind=stringToFind;
//        this.stringToReplace=stringToReplace;
//    }
//	
	@DataBoundConstructor
	public MultiChannelPackerBuilder(String filePath,String stringToFind,String stringToReplace,
			String xmlNodeName,String newNodeNameString){
        this.filePath = filePath;
        this.stringToFind=stringToFind;
        this.stringToReplace=stringToReplace;
        this.xmlNodeName=xmlNodeName;
        this.newNodeNameString=newNodeNameString;
    }
	
	public String getFilePath() {
		return filePath;
	}
	public String getStringToFind() {
		return stringToFind;
	}
	public String getStringToReplace() {
		return stringToReplace;
	}
	
	public String getXmlNodeName() {
		return xmlNodeName;
	}
	public String getNewNodeNameString() {
		return newNodeNameString;
	}
	@Override
	public boolean perform(AbstractBuild build, Launcher launcher, BuildListener listener){
		ReplaceStrInFile re = new ReplaceStrInFile();
		boolean result=re.replaceInFile(filePath, stringToFind, stringToReplace);
		if (result) {
			listener.getLogger().println("[INFO]:完成文件替换在" + filePath + "文件中，将" + stringToFind
					+ "字符串替换成了" + stringToReplace + "！");
			return true;
		}else {
			listener.getLogger().println("[ERROR]:文件字符串替换出现错误，请检查被修改的文件是否存在及其读写权限！");
			return false;
		}
	}
	@Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl)super.getDescriptor();
    }
	
	@Extension
	public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {
        public DescriptorImpl() {
            load();
        }

        public FormValidation doCheckFilePath(@QueryParameter String value)
                throws IOException, ServletException {
            if (value.length() == 0)
                return FormValidation.error("Please set a file path");
            if (!(new File(value).exists()))
                return FormValidation.warning("The file\""+value+"\" is not exists,please check it.");
            return FormValidation.ok();
        }
        public FormValidation doCheckStringToFind(@QueryParameter String value)
                throws IOException, ServletException {
            if (value.length() == 0)
                return FormValidation.error("Please set a string to find.");
            return FormValidation.ok();
        }
        
        public FormValidation doCheckStringToReplace(@QueryParameter String value)
                throws IOException, ServletException {
            if (value.length() == 0)
                return FormValidation.error("Please set a string to replace.");
            return FormValidation.ok();
        }
        
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }

		@Override
		public String getDisplayName() {
			return "Android Multiple Package.";
		}

        @Override
        public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
            return super.configure(req,formData);
        }
	}
}
