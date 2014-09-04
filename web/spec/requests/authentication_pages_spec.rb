require 'rails_helper'

describe "Authentication Pages" do
	before { visit signin_path }
	subject { page }
	describe "GET /signin" do
		it { should have_title(I18n.t(:login)) }
		it { should have_content(I18n.t(:login)) }
	end

	describe "with invalid information" do
		before { click_button I18n.t(:login_button) }
		it { should have_selector('div.error') }
	end

	describe "with valid information" do
		let(:user) { FactoryGirl.create(:user) }
		before do
			fill_in I18n.t(:form_user_email), with: user.email.upcase
			fill_in I18n.t(:form_user_password), with: user.password
			click_button I18n.t(:login_button)
		end
		it { should have_content(user.name << " " << user.surname) }
	end
end
