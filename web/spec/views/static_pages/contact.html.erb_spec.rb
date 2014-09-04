require 'rails_helper.rb'

describe "Contact page" do
	before { visit contact_url }
	subject { page }
	it { should have_content(I18n.t (:contact)) }
	it { should have_title(full_title(I18n.t (:contact))) }
end
