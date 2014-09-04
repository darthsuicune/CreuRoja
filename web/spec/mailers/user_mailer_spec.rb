require "rails_helper"

RSpec.describe UserMailer, :type => :mailer do
	describe "password_reset" do
		let(:user) { FactoryGirl.create(:user) }
		let(:mail) { UserMailer.password_reset(user) }

		it "renders the headers" do
			expect(mail.subject).to eq("Recuperació de contrasenya de Creu Roja Catalunya")
			expect(mail.to).to eq([user.email])
			expect(mail.from).to eq(["tecnicssocors@creuroja.org"])
		end

		it "renders the body" do
			expect(mail.body.encoded).to match("canvi de contrasenya")
		end
	end
end
