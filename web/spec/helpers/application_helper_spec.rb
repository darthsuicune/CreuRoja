require 'rails_helper'

describe ApplicationHelper do
	describe "full title" do
		it "should contain full title" do
			expect(full_title("foo")).to match(/| foo/)
		end
		
		it "should contain base title" do
			expect(full_title("")).to match /[Creu|Cruz] Roja|Red Cross/
		end
		
		it "should not have a |" do
			expect(full_title("")).not_to match(/\|/)
		end
	end
end