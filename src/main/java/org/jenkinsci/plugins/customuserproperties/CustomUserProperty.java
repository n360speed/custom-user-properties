package org.jenkinsci.plugins.customuserproperties;

import hudson.Extension;
import hudson.model.UserProperty;
import hudson.model.UserPropertyDescriptor;
import hudson.model.User;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

public class CustomUserProperty extends UserProperty {
	private List<KeyValuePair> userProperties = null;

	@DataBoundConstructor
	public CustomUserProperty(User user, List<KeyValuePair> userProperties) {
		this.user = user;
		this.userProperties = userProperties;
	}

	public List<KeyValuePair> getUserProperties() {
		return userProperties;
	}

	@Override
	public DescriptorImpl getDescriptor() {
		return (DescriptorImpl)super.getDescriptor();
	}

	@Extension
	public static final class DescriptorImpl extends UserPropertyDescriptor {
		public DescriptorImpl() {
			load();
		}

		public String getDisplayName() {
			return "Custom User Properties";
		}

		public UserProperty newInstance(User user) {
			List<KeyValuePair> keyValuePairs = new ArrayList<KeyValuePair>();

			return new CustomUserProperty(user, keyValuePairs);
		}

		public CustomUserProperty newInstance(StaplerRequest req, JSONObject formData) throws FormException {
			List<KeyValuePair> keyValuePairs = new ArrayList<KeyValuePair>();

			Object obj = formData.get("userProperties");
			if(obj != null) {
				if(obj instanceof JSONObject) {
					JSONObject userPropertiesJson = (JSONObject)obj;
					KeyValuePair keyValuePair = new KeyValuePair();
					keyValuePair.setKey(userPropertiesJson.getString("key"));
					keyValuePair.setValue(userPropertiesJson.getString("value"));
					keyValuePairs.add(keyValuePair);
				}
				else {
					JSONArray userPropertiesJsonArray = (JSONArray)obj;
					for(int i = 0; i < userPropertiesJsonArray.size(); i++) {
						JSONObject userPropertiesJson = userPropertiesJsonArray.getJSONObject(i);
						KeyValuePair keyValuePair = new KeyValuePair();
						keyValuePair.setKey(userPropertiesJson.getString("key"));
						keyValuePair.setValue(userPropertiesJson.getString("value"));
						keyValuePairs.add(keyValuePair);
					}
				}
			}

			return new CustomUserProperty(User.current(), keyValuePairs);
		}
	}
}
