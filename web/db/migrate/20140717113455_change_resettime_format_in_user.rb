class ChangeResettimeFormatInUser < ActiveRecord::Migration
	def change
		for user in User.all
			user.resettime = nil
			user.save!
		end
		change_column :users, :resettime, :datetime
	end
end
