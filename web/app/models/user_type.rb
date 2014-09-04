class UserType < ActiveRecord::Base
	belongs_to :user
	
	validates :user_id, presence: true
	validates :user_type, presence: true
	
	def self.available_types
		[["asi", I18n.t(:asi)],
		 ["acu", I18n.t(:acu)],
		 ["tes", I18n.t(:tes)],
		 ["due", I18n.t(:due)],
		 ["doc", I18n.t(:doc)],
		 ["b1", I18n.t(:b1)],
		 ["btp", I18n.t(:btp)],
		 ["d1", I18n.t(:d1)],
		 ["per", I18n.t(:per)],
		 ["ci", I18n.t(:ci)]]
	end
end
