require 'rails_helper'

RSpec.describe Log, :type => :model do
	before { @log = FactoryGirl.create(:log) }
	subject { @log }
	
	it { should respond_to(:user_id) }
	it { should respond_to(:controller) }
	it { should respond_to(:action) }
	it { should respond_to(:ip) }
end
