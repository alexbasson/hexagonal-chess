$LOAD_PATH.unshift(*Dir[File.join(__dir__, '..', '*', 'lib')])

require_relative 'lib/app'

run App.new
