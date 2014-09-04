require 'rails_helper'

describe "users/index" do
	let(:user) { FactoryGirl.create(:user) }
	before(:each) do
		sign_in user
		assign(:users, [@user = FactoryGirl.create(:user),@user = FactoryGirl.create(:user)])
	end

	it "renders a list of users" do
		render
		# Run the generator again with the --webrat flag if you want to use webrat matchers
		expect(rendered).to match "Name"
		expect(rendered).to match "Surname"
		expect(rendered).to match "Rol|Role"
	end
end
