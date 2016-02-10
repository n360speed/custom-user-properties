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

/**
 * Class extending {@link UserProperty}, which allows you to store {@link List}&lt{@link KeyValuePair}&gt pairs in the user configuration screen.
 * <p>
 * <strong>Example of Groovy code that can be used to access the Custom User Properties key/value pairs<strong>
 * </p>
 * <p>
 * <code><pre>
 * import org.jenkinsci.plugins.customuserproperties.CustomUserProperties;
 * import org.jenkinsci.plugins.customuserproperties.KeyValuePair;
 * 
 * CustomUserProperties getCurrentUserProperties = User.current().getProperty(CustomUserProperties.class);
 * List<KeyValuePair> getCurrentUserKeyValuePairs = getCurrentUserProperties.getUserProperties();
 * 
 * for(i=0;i &lt getCurrentUserKeyValuePairs.size();i++) {
 * 	println('row:' + (i+1).toString());
 * 	println('key: ' + getCurrentUserKeyValuePairs.get(i).getKey());
 * 	println("value: " + getCurrentUserKeyValuePairs.get(i).getValue());
 * 	println("");
 * }
 * // or 
 * println(getCurrentUserProperties.getPropertyValues('test').toString())</pre></code>
 * </p>
 * 
 * @author nabel.sawiris
 */
public class CustomUserProperties extends UserProperty {
	private List<KeyValuePair> userProperties = null;

	/**
	 * @param {@link User}
	 * @param {@link List}&lt{@link KeyValuePair}&gt
	 */
	@DataBoundConstructor
	public CustomUserProperties(User user, List<KeyValuePair> userProperties) {
		this.user = user;
		this.userProperties = userProperties;
	}

	/**
	 * Get the stored {@link List}&lt{@link KeyValuePair}&gt for the user
	 * 
	 * @return {@link List}&lt{@link KeyValuePair}&gt
	 */
	public List<KeyValuePair> getUserProperties() {
		return userProperties;
	}

	/**
	 * Return String List of all values that match Parameter key
	 * 
	 * @param {@link String}
	 * @return {@link List}&lt{@link KeyValuePair}&gt
	 */
	public List<String> getPropertyValues(String key) {
		List<String> result = new ArrayList<String>();
		for(int i = 0; i < userProperties.size(); i++) {
			result.add(userProperties.get(i).getValue());
		}
		return result;
	}

	@Override
	public DescriptorImpl getDescriptor() {
		return (DescriptorImpl)super.getDescriptor();
	}

	/**
	 * Implement extension from {@link UserPropertyDescriptor}
	 * 
	 * @author nabel.sawiris
	 */
	@Extension
	public static final class DescriptorImpl extends UserPropertyDescriptor {

		public String getDisplayName() {
			return "Custom User Properties";
		}

		/**
		 * Creates a default instance of {@link CustomUserProperties} to be associated with {@link User} object that wasn't created from a persisted
		 * XML data.
		 * <p>
		 * See {@link User} class javadoc for more details about the life cycle of {@link User} and when this method is invoked.
		 */
		public UserProperty newInstance(User user) {
			List<KeyValuePair> keyValuePairs = new ArrayList<KeyValuePair>();

			return new CustomUserProperties(user, keyValuePairs);
		}

		/**
		 * Get submitted form data and persist it (save it). Save is automatically handled as long as we create a new instance of
		 * {@link CustomUserProperties}
		 */
		public CustomUserProperties newInstance(StaplerRequest req, JSONObject formData) throws FormException {
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

			return new CustomUserProperties(User.current(), keyValuePairs);
		}
	}
}
