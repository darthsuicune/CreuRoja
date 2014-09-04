require 'rails_helper'

describe "Static Pages" do
	let(:user) { FactoryGirl.create(:admin) }
	subject { page }

	describe "Contact page" do
		before { visit contact_url }
		let(:page_title) { I18n.t (:contact) }
		it { should have_title(full_title(page_title)) }
	end
	describe "About page" do
		before { visit about_url }
		let(:page_title) { I18n.t (:about) }
		it { should have_title(full_title(page_title)) }
	end
	
	describe "Map" do
		let(:page_title) { I18n.t (:map) }
		describe "not signed in" do
			before { visit map_url }
			it { should_not have_title(:page_title) }
		end
		describe "signed in" do
			before { sign_in user
			         visit map_url }
			it { should have_title(full_title(page_title)) }
		end
	end
end
