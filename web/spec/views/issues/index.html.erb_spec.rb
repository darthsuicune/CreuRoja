require 'rails_helper'

RSpec.describe "issues/index", :type => :view do
	before(:each) do
		assign(:issues, [
			Issue.create!(
			:status => "Status",
			:severity => "Severity",
			:short_description => "Short Description",
			:long_description => "Long Description",
			:component => "Component"
			),
			Issue.create!(
			:status => "Status",
			:severity => "Severity",
			:short_description => "Short Description",
			:long_description => "Long Description",
			:component => "Component"
			)
		])
	end

	it "renders a list of issues" do
		render
		assert_select "tr>td", :text => "Status".to_s, :count => 2
		assert_select "tr>td", :text => "Severity".to_s, :count => 2
		assert_select "tr>td", :text => "Short Description".to_s, :count => 2
		assert_select "tr>td", :text => "Long Description".to_s, :count => 2
		assert_select "tr>td", :text => "Component".to_s, :count => 2
	end
end
