require 'rails_helper'

RSpec.describe "issues/new", :type => :view do
	before(:each) do
		assign(:issue, Issue.new(
			:status => "MyString",
			:severity => "MyString",
			:short_description => "MyString",
			:long_description => "MyString",
			:component => "MyString"
		))
	end

	it "renders new issue form" do
		render

		assert_select "form[action=?][method=?]", issues_path, "post" do

			assert_select "input#issue_status[name=?]", "issue[status]"

			assert_select "input#issue_severity[name=?]", "issue[severity]"

			assert_select "input#issue_short_description[name=?]", "issue[short_description]"

			assert_select "input#issue_long_description[name=?]", "issue[long_description]"

			assert_select "input#issue_component[name=?]", "issue[component]"
		end
	end
end
