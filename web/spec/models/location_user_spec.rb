require 'rails_helper'

describe LocationUser do
	let(:user) { FactoryGirl.create(:user) }
	let(:location) { FactoryGirl.create(:location, :location_type => "assembly") }
	let(:location_user) { FactoryGirl.create(:location_user, :location_id => location.id, :user_id => user.id) }

	subject { location_user }
	it { should respond_to(:user) }
	it { should respond_to(:location) }
end
