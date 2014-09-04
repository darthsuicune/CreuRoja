require 'rails_helper'

RSpec.describe "issues/show", :type => :view do
	before(:each) do
		@issue = assign(:issue, Issue.create!(
			:status => "Status",
			:severity => "Severity",
			:short_description => "Short Description",
			:long_description => "Long Description",
			:component => "Component"
		))
	end

	it "renders attributes in <p>" do
		render
		expect(rendered).to match(/Status/)
		expect(rendered).to match(/Severity/)
		expect(rendered).to match(/Short Description/)
		expect(rendered).to match(/Long Description/)
		expect(rendered).to match(/Component/)
	end
end
