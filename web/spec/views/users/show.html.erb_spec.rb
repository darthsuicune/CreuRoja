require 'rails_helper'

describe "users/show" do
	let(:user) { FactoryGirl.create(:user) }
	before(:each) do
		sign_in user
		@user = FactoryGirl.create(:user)
	end

	it "renders attributes in <p>" do
		render
		# Run the generator again with the --webrat flag if you want to use webrat matchers
		expect(rendered).to match(/[Name|Nombre]/)
		expect(rendered).to match(/[Surname|Apellido]/)
		expect(rendered).to match(/[E-mail|Correo]/)
		expect(rendered).to match(/[Language|Idioma]/)
		expect(rendered).to match(/Rol/)
	end
end
