require 'rails_helper'

describe "users/edit" do
	let(:user) { FactoryGirl.create(:admin) }
	before(:each) do
		sign_in user
		@user = FactoryGirl.create(:user)
	end

	it "renders the edit user form" do
		render

		# Run the generator again with the --webrat flag if you want to use webrat matchers
		assert_select "form[action=?][method=?]", user_path(@user), "post" do
			assert_select "input#user_name[name=?]", "user[name]"
			assert_select "input#user_surname[name=?]", "user[surname]"
			assert_select "input#user_email[name=?]", "user[email]"
			assert_select "select#user_language[name=?]", "user[language]"
			assert_select "select#user_role[name=?]", "user[role]"
		end
	end
end
