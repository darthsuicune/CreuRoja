require 'rails_helper'

RSpec.describe Issue, :type => :model do
	before { @issue = FactoryGirl.create(:issue) }
	subject { @issue }
	
	it { should respond_to(:severity) }
	it { should respond_to(:status) }
	it { should respond_to(:severity) }
	it { should respond_to(:short_description) }
	it { should respond_to(:long_description) }
	it { should respond_to(:component) }
end
