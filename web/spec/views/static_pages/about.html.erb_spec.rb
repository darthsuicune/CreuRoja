require 'rails_helper.rb'

describe "About page" do
	before { visit about_url }
	subject { page }
	it { should have_content(I18n.t (:about)) }
	it { should have_title(full_title(I18n.t (:about))) }
end
