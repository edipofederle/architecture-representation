package mestrado.arquitetura.smarty.profile;

import mestrado.arquitetura.helpers.ModelNotFoundException;


public class SMarty {

	public static void main(String[] args) throws ModelNotFoundException {
		ProfileSMarty smartyProfile = new ProfileSMarty("smarty.profile");
		smartyProfile.getProfile().getName();
	}

}
	